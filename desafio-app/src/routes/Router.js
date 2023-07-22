import { createBrowserRouter } from "react-router-dom";
import Public from "../pages/Public";
import ProtectedRoute from "./ProtectedRoute";
import Upload from "../pages/Upload";
import Dashboard from "../pages/Dashboard";
import FileDetails from "../pages/FileDetails";
import Stores from "../pages/Stores";
import StoreReport from "../pages/StoreReport";
import LoginCallback from "../pages/LoginCallback";

const DefaultRouter = createBrowserRouter([
  {
    path: '/',
    element: <Public />
  },
  {
    path: '/upload',
    element: <ProtectedRoute><Upload /></ProtectedRoute>
  },
  {
    path: '/dashboard',
    element: <ProtectedRoute><Dashboard /></ProtectedRoute>
  },
  {
    path: '/files',
    children: [
      {
        path: '/files/:id',
        element: <ProtectedRoute><FileDetails /></ProtectedRoute>
      }
    ]
  },
  {
    path: '/stores',
    element: <ProtectedRoute><Stores /></ProtectedRoute>
  },
  {
    path: '/stores/:id',
    element: <ProtectedRoute><StoreReport /></ProtectedRoute>
  },
  {
    path: '/callback',
    element: <LoginCallback />
  }
]);

export default DefaultRouter;