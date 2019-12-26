<#-- @ftlvariable name="package" type="fi.xylix.Package" -->
<#-- @ftlvariable name="dependencyLinks" type="java.util.List" -->
<#-- @ftlvariable name="reverseDependencies" type="java.util.List" -->
<html>
	<body>
        <ul style="list-style-type:none;">
            <table>
                <tr><td>Name: </td><td>${package.name}</td></tr>
                <tr><td>Description: </td><td>${package.description}</td></tr>
                <tr>
                    <td>Dependencies: </td>
                    <td>
                        <ul style="list-style-type:none;">
                            <#list dependencyLinks as dependency>
                                <li>${dependency}</li>
                            </#list>
                        </ul>
                    </td>
                </tr>
                <tr>
                    <td>Reverse Dependencies: </td>
                    <td>
                        <ul style="list-style-type:none;">
                            <#list reverseDependencies as reverse>
                                <li>${reverse}</li>
                            </#list>
                        </ul>
                    </td>
                </tr>
            </table>

        </ul>
    </body>
</html>
