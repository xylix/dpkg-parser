<#-- @ftlvariable name="package" type="fi.xylix.Package" -->
<#-- @ftlvariable name="packageList" type="java.util.List" -->
<html>
	<body>
        <ul>
            <li>Name: ${package.name}</li>
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
                    <#list package.reverseDependencies(packageList) as reverse>
                        <li>${reverse}</li>
                    </#list>
                </ul>
            </li>

        </ul>
    </body>
</html>
