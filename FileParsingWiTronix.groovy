<html>
<body>
<%

import hudson.*
import jenkins.*
import java.text.*
import groovy.json.*
import groovy.io.FileType
import java.nio.file.Files
try
{
def env = build.environment
def PreviousMetrics = new File("D:/Jenkins/jobs/"+project.name+"/builds/"+build.number+"/archive/PreviousBuildFileCoverageValue")
def CurrentMetrics = new File("D:/Jenkins/jobs/"+project.name+"/builds/"+build.number+"/archive/CurrentBuildFileCoverageValue")
if (PreviousMetrics.exists() && CurrentMetrics.exists()) {
  %>
  <table style="border-collapse: collapse;">
  <tr>
    <th> Files Checked in </th>
    <th> Current Build Coverage </th>
    <th> Previous Build Coverage </th>
  </tr>
  <%
  CurrentMetrics.eachLine { String line ->
  def filename = line.split("=")
  %>
  <tr>
    <td> <% println line.split("=")[0] %> </td>
    <td> <%
          if(filename.length>1)
          {
            println line.split("=")[1]
          }
          else
          {
            println "-"
          }
   %>
    </td>
   <%
    PreviousMetrics.any { String line2 ->
     def filename1 = line2.split("=")
     if(line2.contains(line.split("=")[0]))
     {
      %><td>
      <%
      if(filename1.length>1)
      {
        println line.split("=")[1]
      }
      else
      {
        println "-"
      }
	 return true
      %></td><%
     }
    }
   %>
  </tr>
  <%
  }
  }
  else{
   println "File level coverage metrics not generated. Please contact System team for details."
  }
}

catch(Exception e)
{
    println "$e"
}
%>
</table>
</body>
</html>
