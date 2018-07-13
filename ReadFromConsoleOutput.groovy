<html>
<body>
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
                        if(line.contains("edit \$/") || line.contains("add \$/"))
                        {
                                %><p><%println line%></p><%
                        }

                }
                %>
</body>
</html>
