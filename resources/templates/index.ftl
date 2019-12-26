<#-- @ftlvariable name="packageList" type="java.util.List" -->
<html>
    <head>
        <link rel="stylesheet" href="https://unpkg.com/purecss@1.0.1/build/pure-min.css" integrity="sha384-oAOxQR6DkCoMliIh8yFnu25d7Eq/PHS21PClpwjOTeU2jRSq11vu66rf90/cZr47" crossorigin="anonymous">
        <style type="text/css">body{margin:40px auto;max-width:650px;line-height:1.3;font-size:18px;
                color:#444;padding:0 10px}h1,h2,h3{line-height:1.2}
        </style>
    </head>
	<body>
        <table class="pure-table pure-table-bordered">
            <#list packageList as item>
                <tr>
                    <td>${item}</td>
                </tr>
            </#list>
        </table>
    </body>
</html>
