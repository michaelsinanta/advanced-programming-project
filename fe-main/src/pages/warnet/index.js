import { useState, useEffect } from "react";
import moment from "moment-timezone";
import useWarnetSession from "@/components/warnet/hooks/useSession";
import axios from "axios";
import { useRouter } from "next/router";
import NoActiveSession from "@/components/warnet/NoActiveSession";
import Gaming from "@/components/warnet/Gaming";
import { toast } from "react-toastify";

export default function MyPage() {
  const [targetDatetime, setTargetDatetime] = useState(
    moment().subtract(1, "day")
  );

  const { getSessionId, checkSessionStillActive } = useWarnetSession();
  const [sessionId, setSessionId] = useState("loading...");
  const [notified, setNotified] = useState(false);
  const router = useRouter();
  const [hasActiveSession, setHasActiveSession] = useState();
  const [isTimerActive, setIsTimerActive] = useState(false);
  const [timeRemaining, setTimeRemaining] = useState();

  useEffect(() => {
    setHasActiveSession(getSessionId() !== undefined)
    setSessionId(getSessionId())
  }, [hasActiveSession]);

  useEffect(() => {
    if (sessionId !== "loading...") {
      axios
        .get(`/api/warnet/sewa_pc/get_session/${sessionId}`)
        .then((response) => {
          setTargetDatetime(moment.tz(response.data.datetimeEnd, "UTC"));
          setIsTimerActive(true);
        })
        .catch((error) => {
          console.log(error);
        })
    }
  }, [sessionId]);

  useEffect(() => {
    let timer;
    if (isTimerActive) {
      timer = setInterval(() => {
        setTimeRemaining(calculateTimeRemaining());
      }, 1000);
    } else {
      clearInterval(timer);
    }

    return () => clearInterval(timer);
  }, [targetDatetime, isTimerActive]);

  useEffect(() => {
    if (notified) {
      toast.warning("Waktu tersisa kurang dari 5 menit");
    }
  }, [notified]);

  const stopAlert = () => {
    setIsTimerActive(false);
    setNotified(false);
    setHasActiveSession(false);
  };

  const calculateTimeRemaining = () => {
    const duration = moment.duration(targetDatetime.diff(moment()));
    const days = Math.floor(duration.asDays());
    const hours = duration.hours();
    const minutes = duration.minutes();
    const seconds = duration.seconds();
    if (days === 0 && hours === 0 && minutes < 5 && !notified) {
      setNotified(true);
    }
    if (days < 0) {
      setNotified(false);
      toast.error("Maaf, sesi Anda sudah habis.");
      router.push("/invoice");
    }
    return days < 0
      ? "loading..."
      : `${days}d ${hours}h ${minutes}m ${seconds}s`;
  };

  if (hasActiveSession) {
    return (
      <Gaming
        sessionId={sessionId}
        timeRemaining={timeRemaining}
        setHasActiveSession={setHasActiveSession}
        stopAlert={stopAlert}
      ></Gaming>
    );
  } else {
    return <NoActiveSession />;
  }
}
