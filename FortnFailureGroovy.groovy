<span style=\'line-height: 22px; font-family: Candara; padding: 10.5px; font-size: 15px; word-break: break-all; word-wrap: break-word; \'>
        <h1><FONT COLOR=red>${project.name} - Build # ${build.number} - ${build.result}</FONT></h1>
<%
def env = build.environment
%>

        <h2>${env.Reason}</h2>
        <p><h2><a href="$build.url">Click Here</a> to view build result</h2><br><h3>Please find below, the build logs and other files.</h3></p>
</span>
