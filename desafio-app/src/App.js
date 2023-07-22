import { RouterProvider } from 'react-router-dom';
import './App.css';
import Navbar from './components/Navbar';
import DefaultRouter from './routes/Router';

function App() {
return (
    <div>
      <Navbar />
      <RouterProvider router={DefaultRouter} />
    </div>
  );
}

export default App;
