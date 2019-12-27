<#-- @ftlvariable name="package" type="fi.xylix.Package" -->
<#-- @ftlvariable name="dependencyLinks" type="java.util.List" -->
<#-- @ftlvariable name="reverseDependencies" type="java.util.List" -->

<html>
    <head>
        <link rel="stylesheet" href="https://unpkg.com/purecss@1.0.1/build/pure-min.css" integrity="sha384-oAOxQR6DkCoMliIh8yFnu25d7Eq/PHS21PClpwjOTeU2jRSq11vu66rf90/cZr47" crossorigin="anonymous">
        <style type="text/css">body{margin:40px auto;max-width:650px;line-height:1.6;font-size:18px;
                color:#444;padding:0 10px}h1,h2,h3{line-height:1.2}
        </style>
    </head>
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
