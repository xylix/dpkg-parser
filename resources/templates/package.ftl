<html>
	<body>
        <ul>
            <li>${package.name}</li>
            <li>${package.description}</li>
            <li>
                <ul style="list-style-type:none;">
                    <#list package.dependencies as dependency>
                        <li>${dependency}</li>
                    </#list>
                </ul>

            </li>
            <li>
                <ul style="list-style-type:none;">
                    <#list package.reverseDependencies as reverse>
                        <li>${reverse}</li>
                    </#list>
                </ul>
            </li>

        </ul>
    </body>
</html>


        val name: String, val description: String,
        val dependencies: List<String>, val reverseDependencies: List<String>)
