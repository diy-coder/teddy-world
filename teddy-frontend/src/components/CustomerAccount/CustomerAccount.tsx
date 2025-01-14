import UserOn from "../UserOn/UserOn";
import QuickLinks from "../QuickLinks/QuickLinks";
import Footer from "../Footer/Footer";
import "./CustomerAccount.css";
import { FaUserCircle } from "react-icons/fa";
import { Link, useParams } from "react-router-dom";
import { CustomerContextTiping, useCustomer } from "../../providers/Customer";

export interface CustomerAccountProps {
  children?: React.ReactNode;
}

function CustomerAccount({ children }: CustomerAccountProps) {
  const { customer } = useCustomer() as CustomerContextTiping;
  const { id } = useParams<{ id: string }>();

  return (
    <>
      <UserOn />
      <QuickLinks />
      <main>
        <div className="container ">
          <div className="row p-1 mt-2 ">
            <FaUserCircle size={30} />
            <div className="">
              <label className="font-weight-normal ml-2">
                Olá, {customer?.fullName}
              </label>
            </div>
          </div>
          <div className="row">
            <div className="col-sm-3 border">
              <div className="list-group p-2 ">
                <h6>Seu Cadastro</h6>
                <div>
                  <Link
                    to={`/cliente/${id}/alterar_dados`}
                    className="list-group-item costumer_account_link border-0"
                  >
                    Alterar dados cadastrais
                  </Link>
                  <Link
                    to={`/cliente/${id}/alterar_senha`}
                    className="list-group-item costumer_account_link border-0"
                  >
                    Alterar senha
                  </Link>
                  <Link
                    to={`/cliente/${id}/cartao`}
                    className="list-group-item costumer_account_link border-0"
                  >
                    Cartões
                  </Link>
                  <Link
                    to="/"
                    className="list-group-item costumer_account_link border-0"
                  >
                    Sair
                  </Link>
                </div>

                <div className="list-group ">
                  <h6>Seus Pedidos</h6>
                  <div>
                    <Link
                      to={`/cliente/${id}/pedidos`}
                      className="list-group-item costumer_account_link border-0"
                    >
                      Acompanhar pedidos
                    </Link>
                  </div>
                </div>

                <div className="list-group ">
                  <h6>Serviços</h6>
                  <div>
                    <Link
                      to="/atendimento"
                      className="list-group-item costumer_account_link border-0"
                    >
                      Central de Atendimento
                    </Link>
                  </div>
                </div>
              </div>
            </div>

            <div className="col-sm-9">{children}</div>
          </div>
        </div>
      </main>
      <Footer />
    </>
  );
}

export default CustomerAccount;
