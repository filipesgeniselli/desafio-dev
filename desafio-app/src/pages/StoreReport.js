import React, { useEffect, useState } from 'react';
import api from '../api/api';
import { useAuth } from 'react-oidc-context';
import { useParams } from 'react-router-dom';

const StoreReport = () => {
  const auth = useAuth();
  const [transactions, setTransactions] = useState([]);
  let { id } = useParams();

  const fetchTransactionsData = () => {
    api
      .get(`/transactions/stores/${id}/transactions`, {
        headers: {
          Authorization: `Bearer ${auth.user?.access_token}`
        }
      })
      .then((response) => setTransactions(response.data))
      .catch((err) => {
        alert("Erro ao consultar api.");
        console.log(err);
      });
  }

  useEffect(() => {
    fetchTransactionsData();
  });

  return (
    <div className="container">
      <h1>Lista de transações</h1>
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Tipo</th>
            <th>Valor</th>
            <th>Data</th>
            <th>CPF</th>
            <th>Cartão</th>
          </tr>
        </thead>
        <tbody>
          {transactions.map((transaction, index) => {
            return (
              <tr key={transaction.id}>
                <td>{transaction.id}</td>
                <td>{transaction.type}</td>
                <td>R$ {Number(transaction.amount).toFixed(2)}</td>
                <td>{transaction.transactionTime}</td>
                <td>{transaction.cpf}</td>
                <td>{transaction.card}</td>                
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
};

export default StoreReport;
