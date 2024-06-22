import cookieCutter from "cookie-cutter";
import axios from "axios";
import moment from "moment-timezone";

const useWarnetSession = () => {
  const SESSION_COOKIE = "sessionCookie";
  const getSessionId = () => cookieCutter.get(SESSION_COOKIE);

  const setSessionId = (sessionId) => {
    cookieCutter.set(SESSION_COOKIE, sessionId);
  };

  const removeSession = () => {
    cookieCutter.set(SESSION_COOKIE, "", { expires: new Date(0) });
  };

  const checkSessionStillActive = async () => {
    const sessionId = getSessionId();

    if (sessionId) {
      const response = await axios.get(
        `/api/warnet/sewa_pc/get_session/${sessionId}`
      );
      const targetDatetime = moment.tz(response.data.datetimeEnd, "UTC");
      return !targetDatetime.isBefore(moment());
    }
    return false;
  };

  return { getSessionId, setSessionId, removeSession, checkSessionStillActive };
};

export default useWarnetSession;
