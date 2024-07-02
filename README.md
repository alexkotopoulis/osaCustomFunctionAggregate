# Oracle GoldenGate Stream Analytics Custom Function Example for Aggregating Delimited Lists

Example for building a JAR file to be used in a custom functions in Oracle GoldenGate Stream Analytics (GGSA). 
The custom function aggregates numeric content in a delimited list string inside an event.
Written to be used with Oracle GoldenGate Stream Analytics (Installer or OCI Marketplace) version 19.1.0.0.7+
 
## Requirements
- Oracle GoldenGate Stream Analytics 19.1.0.0.7+ environment (Cannot be used with OCI GoldenGate cloud service)
- JDK 1.8 and Maven build environment

  
## Functionality
The custom JAR contains the following two functions:

* `double sumInList(String input, String delimiter, int tupleSize, int position )`
* `double avgInList(String input, String delimiter, int tupleSize, int position )`

The functions calculate a sum or average of numeric fields (decimal or integer) within the delimited string `input`. 
The parameter `delimiter` defines the delimiter used to separate fields, for example ",". 
If every field in the list is a number that should be aggregated, please choose `tupleSize`=1 and `position`=0. 
If the list consists of tuples, please select `tupleSize`>1 with the actual size, and `position` with the location of the relevant field within the tuple (0 to tupleSize-1).

## Examples

* `sumInList("1,2,3,4",",",1,0)` returns 10.0
* `sumInList("a~4.0~b~2.0~c~3.0~d~4.0","~",2,1)` returns 13.0
* `avgInList("a:4.0:1:b:2.0:0:c:3.0:1:d:4.0",":",3,1)` returns 3.25

## Steps
1. Clone GIT repository to a local directory in build environment
2. Copy the following file from the GGSA environment onto the local directory created in Step 1:  
`$OSA_HOME/osa-base/extensibility-api/osa.spark-cql.extensibility.api.jar`
3. Run the script to create an entry for the extensibility API jar in your local Maven repository  
`sh createLocalMavenOsa.sh`
4. Run Maven install on command line or using development environment  
`mvn install`  
This command will create a Custom Stage JAR at `target/osaCustomFunctionAggregate-0.0.1-SNAPSHOT-jar-with-dependencies`
5. In the GGSA Console, [create a custom JAR object](https://docs.oracle.com/en/middleware/fusion-middleware/osa/19.1/using/adding-custom-functions-and-custom-stages.html#UGOSA-GUID-263756AC-339A-4E38-8C9F-8C310CDD2D34).

6. Create a pipeline that has delimited strings as one of its fields. You can use the examples from above. Add a Query Stage and [create an expression that uses the custom function](https://docs.oracle.com/en/middleware/fusion-middleware/osa/19.1/using/adding-custom-functions-and-custom-stages.html#GUID-485E8FFB-84C6-4690-A803-E0A57B28744E).

7. When running the pipeline, the output of the Custom JAR Function should show the aggregate value of the list. 
