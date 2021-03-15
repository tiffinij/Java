<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Vending Simulator</title>
        <!-- Bootstrap core CSS -->
      <!-- Latest compiled and minified CSS -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
        integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
        
        <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet"> 
        
    </head>
    

    <body>
        <h1>Vending Machine</h1>
        <hr>
        <div id="vending-display" class="container vending-column">
            <div class="row">

                    <c:forEach items="${items}" var="item">
                        <form method="GET" action="itemSelect">
                            <button class="col-md-4 box btn btn-block btn-lg" type="submit" name="itemButton" value="${item.id}">
                                    <p class="item-number text-left"><c:out value="${item.id}"/></p>
                                    <p class="text-center"><c:out value="${item.name}"/></p>
                                    <p class="text-center"><c:out value="$${item.price}"/></p>
                                    <p class="text-center"><c:out value="Quantity: ${item.quantity}"/></p>
                            </button>
                        </form>
                    </c:forEach>
            </div> <!-- END ROW DIV -->
        </div> <!-- END VENDING DISPLAY CONTAINER DIV -->

        <div id="vending-menu" class="container menu-column text-center" style="margin-bottom: 100px;">
            <h2>Money Entered</h2>

            <form method="GET" action="addMoney">
                <input value="$${moneyInserted}" name="addMoney" class="message-box" disabled/>
                <br/>
                <button type="submit" class="btn btn-primary" name="addMoney" value="Add Dollar" >Add Dollar</button>
                <button type="submit" class="btn btn-primary" name="addMoney" value="Add Quarter" >Add Quarter</button>
                <br/>
                <button type="submit" class="btn btn-primary" name="addMoney" value="Add Dime" >Add Dime</button>
                <button type="submit" class="btn btn-primary" name="addMoney" value="Add Nickel" >Add Nickel</button>
            </form>
            <hr>
            <h2>Message</h2>
            <input value="${message}" class="message-box" name="showMessage" disabled/><br/>
            <input value="${selectedItem}" class="message-box" name="showItem" disabled/>
            <form method="GET" action="makePurchase">
                <button type="submit" class="btn btn-lg btn-success">Make Purchase</button>
            </form>
            <hr>
            <h2>Change</h2>
            <input value="${change}" class="message-box" name="show-change" disabled/>
            <form metthod="GET" action="getChange">
                <button type="submit" class="btn btn-lg btn-warning" >Get Change Back</button>
            </form>

        </div><!-- END VENDING MENU CONTAINER DIV -->

        <footer class="col-md-12">
            <p style=" margin-bottom: 50px;">Created by Tiffini Johnson. Updated March 2021.</p>
        </footer>
    </body>

</html>
