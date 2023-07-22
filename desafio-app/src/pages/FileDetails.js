import React, { useEffect, useState } from 'react';
import { useAuth } from 'react-oidc-context';
import { useParams } from 'react-router-dom';
import api from '../api/api';

const FileDetails = () => {
  const auth = useAuth();
  const [lines, setLines] = useState([]);
  const [file, setFile] = useState({});

  let { id } = useParams();

  const fetchFileDetails = () => {
    api
      .get(`/transactions/upload/${id}/lines`, {
        headers: {
          Authorization: `Bearer ${auth.user?.access_token}`
        }
      })
      .then((response) => setLines(response.data))
      .catch((err) => {
        alert("Erro ao consultar api.");
        console.log(err);
      });
  }

  const fetchFileData = () => {
    api
      .get(`/transactions/upload/${id}`, {
        headers: {
          Authorization: `Bearer ${auth.user?.access_token}`
        }
      })
      .then((response) => setFile(response.data))
      .catch((err) => {
        alert("Erro ao consultar api.");
        console.log(err);
      });
  }

  useEffect(() => {
    fetchFileData();
    fetchFileDetails();
  })

  return (
    <div className="container">
      <h1>Detalhes do arquivo</h1>
      <ul>
        <li>Nome arquivo: {file.fileName}</li>
        <li>Inserido em: {file.insertTime}</li>
        <li>Processado em: {file.startProcessTime}</li>
        <li>Finalizado em: {file.endProcessTime}</li>
        <li>Status: {file.status}</li>
        <li>Descrição do erro: {file.errorReason}</li>
      </ul>
      <table>
        <thead>
          <tr>
            <th>Tipo</th>
            <th>Data</th>
            <th>Valor</th>
            <th>CPF</th>
            <th>Cartão</th>
            <th>Hora</th>
            <th>Dono da loja</th>
            <th>Loja</th>
            <th>Descrição</th>
          </tr>
        </thead>
        <tbody>
          {lines.map((line, index) => {
            return (
              <tr key={index}>
                <td>{line.type}</td>
                <td>{line.date}</td>
                <td>{line.amount}</td>
                <td>{line.cpf}</td>
                <td>{line.card}</td>
                <td>{line.time}</td>
                <td>{line.owner}</td>
                <td>{line.store}</td>
                <td>{line.errorDescription}</td>
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
};

export default FileDetails;