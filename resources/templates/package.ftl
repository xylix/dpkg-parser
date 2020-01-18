

<html>
	<body>
        <table class="pure-table pure-table-bordered">
            <tr><td>Name: </td><td>${package.name}</td></tr>
            <tr><td>Description: </td><td>${package.description}</td></tr>
            <tr>
                <td>Dependencies: </td>
                <td>
                    <ul class="pure-menu-list">
                        <#list dependencyLinks as dependency>
                            <li>${dependency}</li>
                        </#list>
                    </ul>
                </td>
            </tr>
            <tr>
                <td>Reverse Dependencies: </td>
                <td>
                    <ul class="pure-menu-list">
                        <#list reverseDependencies as reverse>
                            <li>${reverse}</li>
                        </#list>
                    </ul>
                </td>
            </tr>
        </table>

    </body>
</html>
