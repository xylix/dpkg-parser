<#-- @ftlvariable name="packageList" type="java.util.List" -->
<html>
	<body>
        <table>
            <#list packageList as item>
                    <tr>
                        <td>${item}</td>
                    </tr>
            </#list>
        </table>
    </body>
</html>
