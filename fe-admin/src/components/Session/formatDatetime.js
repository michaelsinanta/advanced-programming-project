import {DateTime} from "luxon";


export const formatDatetime = (datetime) => {
    const userTimeZone = Intl.DateTimeFormat().resolvedOptions().timeZone;
    const utcDatetime = DateTime.fromISO(datetime, { zone: 'utc' });

    return utcDatetime.setZone(userTimeZone).toLocaleString(DateTime.DATETIME_FULL_WITH_SECONDS)
}

export const getTimeZoneAbbreviation = () => {
    const userTimeZone = Intl.DateTimeFormat().resolvedOptions().timeZone;
    const timeZoneAbbreviation = DateTime.local().setZone(userTimeZone).toFormat('ZZZZ')

    return timeZoneAbbreviation
};

export const getNonTimeZonePart = (datetime) => {
    const userTimeZone = Intl.DateTimeFormat().resolvedOptions().timeZone;
    const utcDatetime = DateTime.fromISO(datetime, { zone: 'utc' });

    return utcDatetime.setZone(userTimeZone).toLocaleString({
        month: 'numeric',
        day: 'numeric',
        year: 'numeric',
        hour: 'numeric',
        minute: 'numeric',
        second: 'numeric',
        hour12: true,
    });
};
