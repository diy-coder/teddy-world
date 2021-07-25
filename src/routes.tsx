import { BrowserRouter, Switch, Route } from "react-router-dom";
import Home from "./pages/Home";
import Login from "./pages/Login";
import Register from "./components/Register/Register";
import RememberPass from "./pages/RememberPass";
import Product from "./pages/Product";
import ProductList from "./pages/ProductList";
import CustomerEdit from "./pages/CustomerEdit";
import CustomerPass from "./pages/CustomerPass";
import CustomerOrders from "./pages/CustomerOrders";
import CustomerContactUs from "./pages/CustomerContactUs";
import ContactUs from "./pages/ContactUs";
import Checkout from "./pages/Checkout";
import AdminIndex from "./pages/Admin/Home/Home";
import Cards from "./pages/Cards";
import AdminTeddy from "./pages/Admin/Teddy/AdminTeddy";
import AdminOrders from "./pages/Admin/Orders/AdminOrders";
import AdminDevolutions from "./pages/Admin/Devolutions/AdminDevolutions";


function Routes() {
  return (
    <BrowserRouter>
      <Switch>
        <Route path="/" exact component={Home} />
        <Route path="/login" component={Login} />
        <Route path="/cadastro" component={Register} />
        <Route path="/recuperarsenha" component={RememberPass} />
        <Route path="/atendimento" exact component={CustomerContactUs} />
        <Route path="/atendimento/novo" component={ContactUs} />
        <Route path="/produto" component={Product} />
        <Route path="/produtos" component={ProductList} />
        <Route path="/cliente/alterar_dados" component={CustomerEdit} />
        <Route path="/cliente/alterar_senha" component={CustomerPass} />
        <Route path="/cliente/pedidos" component={CustomerOrders} />
        <Route path="/cliente/:id/checkout" component={Checkout} />
        <Route path="/cliente/:id/cartao" component={Cards} />

        <Route path="/admin" exact component={AdminIndex} />
        <Route path="/admin/teddy" component={AdminTeddy} />
        <Route path="/admin/orders" component={AdminOrders} />
        <Route path="/admin/devolutions" component={AdminDevolutions} />
        
      </Switch>
    </BrowserRouter>
  );
}

export default Routes;
