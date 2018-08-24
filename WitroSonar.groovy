<!DOCTYPE html>

<html lang="en" xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="utf-8" />
    <title></title>

</head>
<body>
    <%

    import hudson.*
    import jenkins.*
    import java.text.*
    import groovy.json.*
    import groovy.io.FileType
    import java.nio.file.Files
    import jenkins.model.Jenkins
    %>
    <!-- SonarQube Results -->
    <h3 style='color:#e46c0a;'> SonarQube Analysis Metrics </h3>

    <%

    def file1 = 'C:/Program Files (x86)/Jenkins/workspace/'+project.name+'/sonarAnalysis.json'
    String fileContents = new File(file1).getText('UTF-16')
    def loopVar = 0
    def jsonSlurper = new JsonSlurper()
    def jsonObject = jsonSlurper.parseText(fileContents)
    def env = build.environment
    def temp = 0
    %>
    <div style="background-color:#F5F5F5;width:50%;padding:2%;margin-bottom:2%">
        <%
        while( loopVar < 200){
        if(jsonObject.component.measures[loopVar] == null)
        {
        break
        }
        if (temp == 0)
        {
        %>
        <div style="float:left;background-color:white;padding:2%;">
            <%
            temp = 1
            }
            else
            {
            %>
            <div style="margin-bottom:2%;background-color:white;padding:2%;">
                <%
                temp = 0
                }
                %>
                <div style="text-align:center;">
                    <p style="margin:0;font-size:large;text-align:center;">
                        <%
                        println jsonObject.component.measures[loopVar].value
                        %>
                    </p>
                    <p style="margin:0;font-size:medium;text-align:center;">
                        <%
                        println jsonObject.component.measures[loopVar].metric
                        %>
                    </p>
                </div>
            </div>
            <%
            loopVar++
            }
            %>
        </div>
     <h3 style='color:#e46c0a;'> SonarQube Coverage Metrics </h3>
                <% if(env.PreviousBuildCoverage) 
                {
                %>   
                <div style="float:left;margin-bottom:2%;padding:2%;margin-right:4%;background-color:#F5F5F5">
                    <p style="margin: 0;font-size:large;text-align:center;">
                        <% println env.PreviousBuildCoverage %>
                    </p>
                    <p style="margin: 0;font-size:medium;text-align:center;">
                        previous_build_coverage
                    </p>
                </div>
                <% } %>
                <div style="float:left;background-color:white;padding:2%;background-color:#F5F5F5">
                        <%
                        if(env.PreviousBuildCoverage < env.CurrentBuildCoverage )
		                {
                           %> <p style="margin: 0;font-size:large;text-align:center;color:Green"><% println env.CurrentBuildCoverage %> </p> <%
                        }
                        else if (env.PreviousBuildCoverage > env.CurrentBuildCoverage)
                        {
                          %>  <p style="margin: 0;font-size:large;text-align:center;color:red"> <% println env.CurrentBuildCoverage %></p><%
                        } else if (env.PreviousBuildCoverage == env.CurrentBuildCoverage) 
                        {
                            %> <p style="margin: 0;font-size:large;text-align:center;color:black"> <% println env.CurrentBuildCoverage %> </p><%
                        }

                        %>
                    <p style="margin: 0;font-size:medium;text-align:center;">
                        current_build_coverage
                    </p>
                </div>
     
</body>
</html>
