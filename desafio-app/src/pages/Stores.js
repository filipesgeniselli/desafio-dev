import React, { useEffect, useState } from 'react';
import { useAuth } from 'react-oidc-context';
import { Link } from 'react-router-dom';
import api from '../api/api';

const Stores = () => {
  const [stores, setStores] = useState([]);
  const auth = useAuth();

  const fetchStoresData = () => {
    api
      .get('/transactions/stores', {
        headers: {
          Authorization: `Bearer ${auth.user?.access_token}`
        }
      })
      .then((response) => setStores(response.data))
      .catch((err) => {
        alert("Erro ao consultar api.");
        console.log(err);
      });
  }

  useEffect(() => {
    fetchStoresData();
  }, []);

  return (
    <div className="container">
      <h1>Lista de lojas cadastradas</h1>
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Nome</th>
            <th>Responsável</th>
            <th>Saldo</th>
          </tr>
        </thead>
        <tbody>
          {stores.map((store, index) => {
            return (
              <tr key={store.id}>
                <td>{store.id}</td>
                <td><Link to={`/stores/${store.id}`}>{store.name}</Link></td>
                <td>{store.owner}</td>
                <td>R$ {Number(store.balance).toFixed(2)}</td>
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
};

export default Stores;