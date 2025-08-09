<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!-- import thư viện spring-form -->
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description"
          content="Responsive Admin &amp; Dashboard Template based on Bootstrap 5">
    <meta name="author" content="AdminKit">
    <meta name="keywords"
          content="adminkit, bootstrap, bootstrap 5, admin, dashboard, template, responsive, css, sass, html, theme, front-end, ui kit, web">

    <link rel="preconnect" href="https://fonts.gstatic.com">


    <title>Admin page</title>
    <link href="${base}/css/app.css" rel="stylesheet">
    <link rel="stylesheet" href="${base}/css/product.css">
    <link
            href="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.12/summernote-lite.css"
            rel="stylesheet">
    <link
            href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600&display=swap"
            rel="stylesheet">
</head>
<jsp:include page="/WEB-INF/views/administrator/layout/css.jsp"></jsp:include>
<body>
<div class="wrapper">
    <nav id="sidebar" class="sidebar js-sidebar">
        <div class="sidebar-content js-simplebar">
            <a class="sidebar-brand" href="${base}/admin/product/list">
                <span class="align-middle">Admin page</span>
            </a>

            <ul class="sidebar-nav">
                <li class="sidebar-header">
                    Pages
                </li>

                <li class="sidebar-item">
                    <a class="sidebar-link" href="${base}/admin/product/list">
                        <i class="align-middle" data-feather="sliders"></i> <span
                            class="align-middle">Dashboard</span>
                    </a>
                </li>

                <li class="sidebar-item">
                    <a class="sidebar-link" href="${base}/admin/profile">
                        <i class="align-middle" data-feather="user"></i> <span class="align-middle">Profile</span>
                    </a>
                </li>

                <li class="sidebar-item"><a class="sidebar-link"
                                            href="${base}/logout"> <i class="align-middle"
                                                                      data-feather="log-in"></i> <span class="align-middle">logout
								</span>
                </a></li>

                <li class="sidebar-item">
                    <a class="sidebar-link" href="${base}/admin/product">
                        <i class="align-middle" data-feather="inbox"></i> <span class="align-middle"> Product</span>
                    </a>
                </li>

                <li class="sidebar-item active">
                    <a class="sidebar-link" href="${base}/admin/category">
                        <i class="align-middle" data-feather="bar-chart-2"></i> <span
                            class="align-middle">Category</span>
                    </a>
                </li>

                <li class="sidebar-item"><a class="sidebar-link"
                                            href="${base}/admin/order"> <i class="align-middle" data-feather="map"></i> <span
                        class="align-middle">Order</span>
                </a></li>
            </ul>
        </div>
    </nav>

    <div class="main">
        <nav class="navbar navbar-expand navbar-light navbar-bg">
            <a class="sidebar-toggle js-sidebar-toggle"> <i
                    class="hamburger align-self-center"></i>
            </a>

            <div class="navbar-collapse collapse">
                <ul class="navbar-nav navbar-align">
                    <li class="nav-item dropdown"><a
                            class="nav-icon dropdown-toggle" href="#" id="alertsDropdown"
                            data-bs-toggle="dropdown">
                        <div class="position-relative">
                            <i class="align-middle" data-feather="bell"></i> <span
                                class="indicator">4</span>
                        </div>
                    </a></li>
                    <li class="nav-item dropdown"><a
                            class="nav-icon dropdown-toggle" href="#" id="messagesDropdown"
                            data-bs-toggle="dropdown">
                        <div class="position-relative">
                            <i class="align-middle" data-feather="message-square"></i>
                        </div>
                    </a></li>
                    <li class="nav-item dropdown"><a
                            class="nav-icon dropdown-toggle d-inline-block d-sm-none"
                            href="#" data-bs-toggle="dropdown"> <i class="align-middle"
                                                                   data-feather="settings"></i>
                    </a> <a class="nav-link dropdown-toggle d-none d-sm-inline-block"
                            href="#" data-bs-toggle="dropdown"> <img
                            src="${base}/img/images.png"
                            class="avatar img-fluid rounded me-1" alt=""/> <span
                            class="text-dark"></span>
                    </a>
                        <div class="dropdown-menu dropdown-menu-end">
                            <a class="dropdown-item" href="${base}/admin/profile"><i
                                    class="align-middle me-1" data-feather="user"></i> Profile</a> <a
                                class="dropdown-item" href="#"><i class="align-middle me-1"
                                                                  data-feather="pie-chart"></i> Analytics</a>
                            <div class="dropdown-divider"></div>
                            <a class="dropdown-item" href="#"><i
                                    class="align-middle me-1" data-feather="settings"></i> Settings
                            & Privacy</a> <a class="dropdown-item" href="#"><i
                                class="align-middle me-1" data-feather="help-circle"></i> Help
                        Center</a>
                            <div class="dropdown-divider"></div>
                            <a class="dropdown-item" href="${base}/logout">Log out (${userLogined.email})</a>
                        </div>
                    </li>
                </ul>
            </div>
        </nav>
        <main>
            <div class="container main-container">
                <h1 class="desc">Thêm mới danh mục sản phẩm</h1>
                <sf:form modelAttribute="category" action="${base}/admin/category"
                         method="post">
                    <div class="form">
                        <label class="form-label">Tên sản phẩm</label>
                        <sf:input path="name" class="name-product" type="text"
                                  placeholder="Nhập tên danh mục mới"></sf:input>
                    </div>
                  <div class="form">
                    <label class="form-label">Mô tả sản phảm</label>
                    <sf:textarea path="description" class="desc-product" name="" id=""></sf:textarea>
                  </div>
                    <div class="btn-confirm">
                        <button type="submit">Xác nhận</button>

                    </div>
                </sf:form>


            </div>
        </main>
    </div>
</div>
<script src="${base}/js/jquery-3.6.1.min.js"></script>
<script src="${base}/js/app.js"></script>
<script
        src="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.12/summernote-lite.js">
</script>
<script>
    $(document).ready(function () {
        $('#details').summernote({});
    });
</script>
</body>
</html>
