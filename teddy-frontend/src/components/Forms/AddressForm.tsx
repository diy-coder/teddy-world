import { useRef, useState } from "react";
import { Form } from "@unform/web";
import InputText from "../Form/InputText";
import { Select } from "../Form/SelectInput";
import { FormHandles } from "@unform/core";
import * as Yup from "yup";
import axios from "axios";
import { UfToName } from "../Utils/ParseUfToName";
import Swal from "sweetalert2";
import { useEffect } from "react";
import { Customer } from "../CustomerAccount/CustomerAccount";
import { SaveAddress } from "../../service/addressService";

enum AddressType {
  BILLING = 0,
  DELIVERY = 1,
}

export interface Address {
  id?: string;
  postalCode: string;
  street: string;
  complement: string;
  number: string;
  state: string;
  city: string;
  neighborhood: string;
  country: string;
  addressType: AddressType;
  customer: {
    id: number;
  };
}

interface AddressFormProps {
  className?: string;
  customer?: Customer;
  address?: Address;
  setCustomer?: Function;
  setIsFormOpen?: Function;
}

export function AddressForm({
  className,
  customer,
  address,
  setCustomer,
  setIsFormOpen,
}: AddressFormProps) {
  const formRef = useRef<FormHandles>(null);
  const [customerAddress, setCustomerAddress] = useState<Customer>();

  useEffect(() => {
    setCustomerAddress(customer);
    if (address) formRef.current?.setData(address);
  }, [customer, address]);

  function fillAddress(value: string) {
    if (value.trim().length > 7) {
      axios
        .get(`https://viacep.com.br/ws/${value}/json/`)
        .then((response) => {
          const data = response.data;
          formRef.current?.setData({
            street: data.logradouro,
            city: data.localidade,
            neighborhood: data.bairro,
            state: UfToName(data.uf),
            country: "Brasil",
          });
        })
        .catch((err) => {
          console.log(err);
        });
    }
  }

  async function handleSubmit(data: Address) {
    try {
      const schema = Yup.object().shape({
        postalCode: Yup.string().required("CEP é obrigatório"),
        street: Yup.string().required("Rua é obrigatório"),
        number: Yup.string().required("Número é obrigatório"),
        neighborhood: Yup.string().required("Bairro é obrigatório"),
        state: Yup.string().required("Estado é obrigatório"),
        city: Yup.string().required("Cidade é obrigatório"),
        country: Yup.string().required("País é obrigatório"),
        addressType: Yup.string().required("Selecione o tipo do endereço"),
      });

      await schema.validate(data, {
        abortEarly: false,
      });

      formRef.current?.setErrors({});

      data.customer = {
        id: Number(customerAddress?.id),
      };

      data.id = address?.id;

      const onSuccess = (resp: any) => {
        setCustomer?.((prev: Customer) => {
          const newCustomerAddress = Object.assign({}, prev);

          if (address?.id) {
            let oldAddressIndex = newCustomerAddress.addressList?.findIndex(
              (el) => el.id === address.id
            );

            if (oldAddressIndex && newCustomerAddress.addressList) {
              newCustomerAddress.addressList[oldAddressIndex] = resp?.data;
            }
            return newCustomerAddress;
          }

          newCustomerAddress.addressList?.push(resp?.data);

          return newCustomerAddress;
        });

        setIsFormOpen?.(false);

        Swal.fire({
          icon: "success",
          title: "Parabéns",
          text: `Seu endereço foi ${
            data.id ? "atualizado" : "criado"
          } com sucesso!`,
        });
      };

      const onError = (resp: any) => {
        Swal.fire({
          icon: "error",
          title: "Oops...",
          text: `<h5>Algo deu errado por aqui</h5>
          <p>${resp}</p>`,
        });
      };

      SaveAddress({ data, onError, onSuccess });

    } catch (error) {
      console.log(error);
      if (error instanceof Yup.ValidationError) {
        const errorMessage: { [key: string]: string } = {};

        error.inner.forEach((err) => {
          if (err.path) errorMessage[err.path] = err.message;
        });

        formRef.current?.setErrors(errorMessage);
      }
    }
  }

  return (
    <Form
      className={className}
      onSubmit={handleSubmit}
      ref={formRef}
      initialData={address}
    >
      <div className="form-group">
        <label htmlFor="postalCode">CEP</label>
        <InputText
          name="postalCode"
          className="form-control"
          onChange={(val) => {
            fillAddress(val.currentTarget.value);
          }}
        />
      </div>

      <div className="form-group">
        <label htmlFor="street">Logradouro</label>
        <InputText name="street" className="form-control" />
      </div>

      <div className="form-group">
        <label htmlFor="number">Número</label>
        <InputText name="number" className="form-control" />
      </div>

      <div className="form-group">
        <label htmlFor="neighborhood">Bairro</label>
        <InputText name="neighborhood" className="form-control" />
      </div>

      <div className="form-group">
        <label htmlFor="city">Cidade</label>
        <InputText name="city" className="form-control" />
      </div>

      <div className="form-group">
        <label htmlFor="complement">Complemento</label>
        <InputText name="complement" className="form-control" />
      </div>

      <div className="form-group">
        <label htmlFor="state">Estado</label>
        <InputText name="state" className="form-control" />
      </div>

      <div className="form-group">
        <label htmlFor="country">País</label>
        <InputText name="country" className="form-control" />
      </div>

      <div className="form-group">
        <label htmlFor="addressType">Tipo do Endereço</label>
        <Select name="addressType" className="form-control" defaultValue="">
          <option value="">Selecione</option>
          <option value={AddressType.BILLING}>Cobrança</option>
          <option value={AddressType.DELIVERY}>Entrega</option>
        </Select>
      </div>

      <button className="buttom btn-block">
        {address ? "Atualizar" : "Adicionar"}
      </button>
    </Form>
  );
}
