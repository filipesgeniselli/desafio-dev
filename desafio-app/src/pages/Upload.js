import React, { useState } from 'react';
import { useAuth } from 'react-oidc-context';
import { useNavigate } from 'react-router-dom';

const Upload = () => {
  const [file, setFile] = useState(null);
  const [isSubmitting, setStatus] = useState(() => false);
  const navigate = useNavigate();
  const auth = useAuth();

  const handleFileChange = (event) => {
    const selectedFile = event.target.files[0];
    setFile(selectedFile);
  };

  const handleSubmit = async (event) => {
    setStatus(true);
    event.preventDefault();

    if (!file) {
      alert('Por favor selecione um arquivo.');
      return;
    }

    try {
      const formData = new FormData();
      formData.append('file', file);

      // Replace 'YOUR_API_URL' with the actual API endpoint where you want to upload the file.
      const apiUrl = 'http://localhost:8081/transactions/upload';

      const response = await fetch(apiUrl, {
        method: 'POST',
        headers: {
          Authorization: `Bearer ${auth.user?.access_token}`
        },
        body: formData,
      });

      if (response.ok) {
        alert('Arquivo enviado com sucesso!');
        navigate('/dashboard');
      } else {
        alert('Error uploading file.');
      }
    } catch (error) {
      console.error('Error uploading file:', error);
    }
    setStatus(false);
  };

  return (
    <div className='container'>
      <h2>CNAB Upload</h2>
      <p>Utilize o formulário abaixo para enviar um arquivo CNAB para procesamento</p>
      <form onSubmit={handleSubmit}>
        <div className="file-input-container">
          <input type="file" className="file-input" accept='.txt' onChange={handleFileChange} />
          <button type="submit" className="submit-button" disabled={isSubmitting}>Submit</button>
        </div>
      </form>
      <p>Considere o formato abaixo durante a criação do arquivo:</p>
      <table>
        <thead>
          <tr>
            <th>Descrição do campo</th>
            <th>Início</th>
            <th>Fim</th>
            <th>Tamanho</th>
            <th>Comentário</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td>Tipo</td>
            <td>1</td>
            <td>1</td>
            <td>1</td>
            <td>Tipo da transação</td>
          </tr>
          <tr>
            <td>Data</td>
            <td>2</td>
            <td>9</td>
            <td>8</td>
            <td>Data da ocorência no formato YYYYMMDD</td>
          </tr>
          <tr>
            <td>Valor</td>
            <td>10</td>
            <td>19</td>
            <td>10</td>
            <td>Valor da movimentação. Obs. O valor encontrado no arquivo precisa ser divido por cem(valor / 100.00) para normalizá-lo.</td>
          </tr>
          <tr>
            <td>CPF</td>
            <td>20</td>
            <td>30</td>
            <td>11</td>
            <td>CPF do beneficiário</td>
          </tr>
          <tr>
            <td>Cartão</td>
            <td>31</td>
            <td>42</td>
            <td>12</td>
            <td>Cartão utilizado na transação</td>
          </tr>
          <tr>
            <td>Hora</td>
            <td>43</td>
            <td>48</td>
            <td>6</td>
            <td>Hora da ocorrência atendendo ao fuso de UTC-3 no formato HHMMSS</td>
          </tr>
          <tr>
            <td>Dono da loja</td>
            <td>49</td>
            <td>62</td>
            <td>14</td>
            <td>Nome do representante da loja</td>
          </tr>
          <tr>
            <td>Nome da loja</td>
            <td>63</td>
            <td>81</td>
            <td>19</td>
            <td>Nome da loja</td>
          </tr>
        </tbody>
      </table>

      <p>Os tipos de transação disponíveis são</p>
      <table>
        <thead>
          <tr>
            <th>Tipo</th>
            <th>Descrição</th>
            <th>Natureza</th>
            <th>Sinal</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td>1</td>
            <td>Débito</td>
            <td>Entrada</td>
            <td>+</td>
          </tr>
          <tr>
            <td>2</td>
            <td>Boleto</td>
            <td>Saída</td>
            <td>-</td>
          </tr>
          <tr>
            <td>3</td>
            <td>Financiamento</td>
            <td>Saída</td>
            <td>-</td>
          </tr>
          <tr>
            <td>4</td>
            <td>Crédito</td>
            <td>Entrada</td>
            <td>+</td>
          </tr>
          <tr>
            <td>5</td>
            <td>Recebimento Empréstimo</td>
            <td>Entrada</td>
            <td>+</td>
          </tr>
          <tr>
            <td>6</td>
            <td>Vendas</td>
            <td>Entrada</td>
            <td>+</td>
          </tr>
          <tr>
            <td>7</td>
            <td>Recebimento TED</td>
            <td>Entrada</td>
            <td>+</td>
          </tr>
          <tr>
            <td>8</td>
            <td>Recebimento DOC</td>
            <td>Entrada</td>
            <td>+</td>
          </tr>
          <tr>
            <td>9</td>
            <td>Aluguel</td>
            <td>Saída</td>
            <td>-</td>
          </tr>
        </tbody>
      </table>
    </div>
  );
};

export default Upload;