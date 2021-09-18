import { Item } from "../Types/cart";
import { axiosInstance } from "./serviceInstance";
import { ServiceTypes } from "./serviceTypes";

export async function AddCartItem({
  onSuccess,
  onError,
  data,
  id
}: ServiceTypes<Item>) {
  let item = undefined;

  await axiosInstance.post(`/customer/${id}/cart`, data, {
    headers: {
      "Access-Control-Allow-Origin": "*",
      "Access-Control-Allow-Methods": "*",
    },
  })
    .then((resp) => {
      item = resp.data;
      onSuccess?.(resp);
    })
    .catch((err) => {
      console.log(err);
      onError?.(err);
    });

  return item;
}

export async function RemoveItem({
  onSuccess,
  onError,
  data,
  id
}: ServiceTypes<Item>) {
  let cart = undefined;
  axiosInstance
    .patch(`/customer/${id}/cart`, data)
    .then((resp) => {
      if (resp.data?.hasError) throw new Error(resp.data?.message);
      onSuccess?.(resp);
      cart = resp.data;
    })
    .catch((err) => {
      console.log(err);
      onError?.(err);
    });

  return cart;
}