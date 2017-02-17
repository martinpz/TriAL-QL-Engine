/*
* Copyright (C) 2017 University of Freiburg.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*
* The TriAL-QL Engine is a research project at the department of 
* computer science, University of Freiburg. 
*
* More information on the project:
* http://dbis.informatik.uni-freiburg.de/forschung/projekte/DiPoS/
* zablocki@informatik.uni-freiburg.de
*/

package loader;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;

import data.structures.RDFgraph;

import org.apache.spark.api.java.function.Function;

public class VerticalPartitionerSpark {
	
	/**
	 * SPARK Vertical Partitioner.
	 * @param inputPath
	 * @param outputPath
	 */
	public static void partition(String inputPath, String outputPath) {
		long lStartTime = System.nanoTime();

		SparkConf sparkConf = new SparkConf().setAppName("JavaSparkSQL").setMaster("local");
		JavaSparkContext ctx = new JavaSparkContext(sparkConf);
		SQLContext sqlContext = new SQLContext(ctx);

		System.out.println("=== Data source: RDD ===");
		
		@SuppressWarnings("serial")
		JavaRDD<RDFgraph> RDF = ctx.textFile(inputPath + "/*").map(new Function<String, RDFgraph>() {
			@Override
			public RDFgraph call(String line) {

				String[] parts = line.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

				RDFgraph entry = new RDFgraph();
				if (parts.length > 2) {
					entry.setSubject(parts[0]);
					entry.setPredicate(parts[1]);
					entry.setObject(parts[2]);
				}
				return entry;

			}
		});

		DataFrame rawGraph = sqlContext.createDataFrame(RDF, RDFgraph.class);
		rawGraph.registerTempTable("rawGraph");

		int numPredicates = sqlContext
				.sql("SELECT predicate FROM rawGraph WHERE subject != '@prefix' GROUP BY predicate").collect().length;

		DataFrame pureGraph = sqlContext
				.sql("SELECT subject, predicate, object FROM rawGraph WHERE subject != '@prefix'");
		DataFrame partitionedGraph = pureGraph.repartition(numPredicates, new Column("predicate"));

		partitionedGraph.write().parquet(outputPath);

		long lEndTime = System.nanoTime();
		long difference = lEndTime - lStartTime;

		System.out.println("Partitioning complete.\nElapsed milliseconds: " + difference / 1000000);

	}
}
