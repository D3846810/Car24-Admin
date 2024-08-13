package uk1.ac.tees.mad.d3846810.navigations


sealed class NavItem(var title: String) {
    object Home : NavItem("Home")
    object Category : NavItem("Category")
    object AllProducts : NavItem("AllProducts")
    object AllBookings : NavItem("AllBookings")
    object CreateSlider : NavItem("CreateSlider")
    object Notifications : NavItem("Notifications")
    object Login : NavItem("Login")
    object AddProduct : NavItem("AddProduct")
    object EditProduct : NavItem("EditProduct")
}

