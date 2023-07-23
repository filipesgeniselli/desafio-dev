import React, { useEffect, useState } from 'react';
import { useAuth } from 'react-oidc-context';
import { Link } from 'react-router-dom';
import api from '../api/api';

const Dashboard = () => {
  const auth = useAuth();
  const [files, setFiles] = useState([]);

  const fetchFileData = () => {
    api
      .get(`/transactions/upload`, {
        headers: {
          Authorization: `Bearer ${auth.user?.access_token}`
        }
      })
      .then((response) => setFiles(response.data))
      .catch((err) => {
        alert("Erro ao consultar api.");
        console.log(err);
      });
  }

  useEffect(() => {
    fetchFileData();
  }, []);

  return (
    <div className="container">
      <h1>Dashboard de uploads</h1>
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Nome arquivo</th>
            <th>Inserido em</th>
            <th>Processado em</th>
            <th>Finalizado em</th>
            <th>Status</th>
            <th>Descrição erro</th>
          </tr>
        </thead>
        <tbody>
          {files.map((file, index) => {
            return (
              <tr key={file.id}>
                <td>{file.id}</td>
                <td><Link to={`/files/${file.id}`}>{file.fileName}</Link></td>
                <td>{file.insertTime}</td>
                <td>{file.startProcessTime}</td>
                <td>{file.endProcessTime}</td>
                <td>{file.status}</td>
                <td>{file.errorReason}</td>
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
};

export default Dashboard;