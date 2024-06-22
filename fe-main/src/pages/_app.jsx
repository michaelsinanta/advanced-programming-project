import { OrderContextProvider } from "@/components/contexts/OrderContext";
import { SessionInfoProvider } from '@/components/warnet/context/SessionContext';
import "@/styles/globals.css";
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

export default function App({ Component, pageProps }) {
  return (
    <SessionInfoProvider>
      <OrderContextProvider>
        <Component {...pageProps} />
        <ToastContainer />
      </OrderContextProvider>
    </SessionInfoProvider>
  );
}
