<#-- @ftlvariable name="packageList" type="java.util.List" -->
<html>
	<body>
        <ul style="list-style-type:none;">
            <#list packageList as item>
                <tr><td>${item}</td></tr>
            </#list>
        </ul>
    </body>
</html>
