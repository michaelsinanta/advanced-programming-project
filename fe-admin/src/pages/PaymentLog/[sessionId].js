import { useRouter } from "next/router";
import PaymentLogDetail from "@/components/PaymentLogs/PaymentLogDetail";
import Navbar from "@/components/Navbar/Navbar";
import TowerLoader from "@/components/TowerLoader/TowerLoader";

const PaymentLog = () => {
  const router = useRouter();
  const { sessionId } = router.query;

  if (sessionId === undefined) {
    return (
      <div className="items-center">
        <h1 className="text-2xl text-center font-bold">Payment Log Detail</h1>
        <div className="flex justify-center items-center h-full mt-32">
          <TowerLoader />
        </div>
      </div>
    );
  }

  return (
    <>
      <div className="font-sans">
        <Navbar />
        <PaymentLogDetail sessionId={sessionId} />
      </div>
    </>
  );
};

export default PaymentLog;
