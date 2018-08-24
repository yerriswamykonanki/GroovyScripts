<span style=\'line-height: 22px; font-family: Candara; padding: 10.5px; font-size: 15px; word-break: break-all; word-wrap: break-word; \'>
 <h1><FONT COLOR=Green>${project.name} - Build # ${build.number} - ${build.result}</FONT></h1>
 <h2 style=\'color:#e46c0a\'>GitHub Details</h2>
 <%
  import hudson.*
  import jenkins.*
  import java.text.*
  import groovy.json.*
  import groovy.io.FileType
  import java.nio.file.Files
 %>
 <%
        def env3 = build.getLogFile()
        def count = 0
         for(def temp in env3)
         {
                count+=1
         }
                def temp1 = build.getLog(count)
                for (def line in build.getLog(count))
                {
                        if(line.contains("Started by")||line.contains("Checking out Revision")||line.contains("Commit message:"))
                        {
                                %><p><%println line%></p><%
                        }
                        if(line.contains("http://padlcicdggk4.sw.fortna.net:8088/artifactory/"))
                        {
                                 %><h2 style=\'color:#e46c0a\'>Artifactary Details</h2>
                                 <p><%println line%><p><%
                        }

                }

 %>
 <table><tr><td>
<%
def env = build.environment
File f = new File(env.JobWorkSpace+'target/surefire-reports');
if (f.exists()) {
%>
<!-- Unit Test Results -->
<h2 style='color:#e46c0a; font-family: Candara;'>Unit Test Results</h2>
<table cellspacing="0" cellpadding="8" border="1" align="left">
<%
 // def dir = new File("/var/lib/jenkins/workspace/"+project.name+"/target/surefire-reports")
   def dir = new File(env.JobWorkSpace)
  dir.eachFileRecurse (FileType.FILES) { file ->
  check = 1
  if(file.getName().contains('.txt')){
        file.eachLine
        { line ->
          if(line.contains('Test '))
          {
            %><tr>
                <td colspan="2" bgcolor="#F3F3F3" style='font-family: Candara;'><%println line %></td>
              </tr><%
          }
          else if(line.contains('------'))
               {
                 return;
               }
               else
               {
                if(check)
                {
                 %><tr><td style='font-family: Candara;'><%
                        def index = line.indexOf(" - in ");
                        def content = line.substring(0 , index)
                        println content ;
                        check = 0
                 %></td></tr><%
                }
                else
                {
                 return;
                }
               }
        }
  }
  }
%>
</table>
<%
}
%>
</td></tr>



 <tr><td>
<!-- SonarQube Results -->
<%
 def folder = new File(env.JobWorkSpace+'/output.json')
 if(folder.exists())
{
%>
<h2 style='color:#e46c0a; font-family: Candara;'>SonarQube Analysis Details</h2>
<%
def file1 = env.JobWorkSpace+'/output.json'
String fileContents = new File(file1).getText('UTF-8')
def loopVar = 0
 %>
<!--def env = System.getenv()
def BUILD_NUMBER= env[""] -->

<%
def jsonSlurper = new JsonSlurper()
def jsonObject = jsonSlurper.parseText(fileContents) %>
<div><p style='font-family: Candara;'>Click here for <a href="http://10.240.17.12:9000/sonar/dashboard/index/${project.name}">Detailed Report</a></p></div>
<table cellspacing="0" cellpadding="8" border="1" align="left">
<thead>
<tr bgcolor="#F3F3F3">
 <th style='font-family: Candara;'>Metric</th>
 <th style='font-family: Candara;'>Value</th>
</tr>
</thead>
<tbody>
<%
while( loopVar < 20){
if(jsonObject.msr[0].key[loopVar] == null)
{
break
}
%>
<tr><td style='font-family: Candara;'><% println jsonObject.msr[0].key[loopVar] %></td>
<td style='font-family: Candara;'><% println jsonObject.msr[0].val[loopVar] %></td></tr>
<%
loopVar++
}
%>

<!--
<tr>
<td colspan="3"  style='font-family: Candara;'><% println jsonObject.msr[0].key %></td> </tr> -->
</tbody>
</table>
 </td></tr>
 <tr><td>
<%
}
%>

<!-- Robot Framework Results -->
 <%
 def robotResults = false
 def actions = build.actions // List<hudson.model.Action>
 actions.each() { action ->
 // def tempvar = action.class.simpleName
  if(action.displayName.equals("Robot Results") ) { // hudson.plugins.robot.RobotBuildAction
   robotResults = true %>
 <p><h2 style='color:#e46c0a; font-family: Candara;'>Robot Framework Results</h2></p>
 <p style='font-family: Candara;'>Click here for <a href="${rooturl}${build.url}robot/report/report.html">Detailed Report</a></p>
 <p style='font-family: Candara;'>Pass Percentage: <%= action.overallPassPercentage %>%</p>
 <table cellspacing="0" cellpadding="4" border="1" align="left">
 <thead>
 <tr bgcolor="#F3F3F3">
  <td style='font-family: Candara;'><b>Test Name</b></td>
  <td style='font-family: Candara;'><b>Status</b></td>
  <td style='font-family: Candara;'><b>Execution Datetime</b></td>
 </tr>
 </thead>
 <tbody>
 <% def suites = action.result.allSuites
   suites.each() { suite ->
    def currSuite = suite
    def suiteName = currSuite.displayName
    // ignore top 2 elements in the structure as they are placeholders
    while (currSuite.parent != null && currSuite.parent.parent != null) {
     currSuite = currSuite.parent
     suiteName = currSuite.displayName + "." + suiteName
    } %>
 <tr><td colspan="3" style='font-family: Candara;'><b><%= suiteName %></b></td></tr>
 <%  DateFormat format = new SimpleDateFormat("yyyyMMdd HH:mm:ss.SS")
    def execDateTcPairs = []
    suite.caseResults.each() { tc ->
      Date execDate = format.parse(tc.starttime)
      execDateTcPairs << [execDate, tc]
    }
    // primary sort execDate, secondary displayName
    execDateTcPairs = execDateTcPairs.sort{ a,b -> a[1].displayName <=> b[1].displayName }
    execDateTcPairs = execDateTcPairs.sort{ a,b -> a[0] <=> b[0] }
 execDateTcPairs.each() {
      def execDate = it[0]
      def tc = it[1]  %>
 <tr>
  <td style='font-family: Candara;'><%= tc.displayName %></td>
  <td style="color: <%= tc.isPassed() ? "#66CC00" : "#FF3333" %>; font-family: Candara;"><%= tc.isPassed() ? "PASS" : "FAIL" %></td>
  <td style='font-family: Candara;'><%= execDate %></td>
 </tr>
 <%  } // tests
   } // suites %>
 </tbody></table><%
  } // robot results
 }
 if (!robotResults) { %>
 <p>No Robot Framework test results found.</p>
 <%
 } %>



 </td></tr>
 <tr><td><br><h2><a href="$build.url">Click Here</a> to view build result</h2><h3>Please find below, the build logs and other files.</h3></td></tr></table>
 </span>
