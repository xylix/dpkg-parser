<#-- @ftlvariable name="data" type="fi.xylix.IndexData" -->
<html>
	<body>
        <ul>
            <#list data.items as item>
                <tr>${item}</tr>
            </#list>
        </ul>
    </body>
</html>
