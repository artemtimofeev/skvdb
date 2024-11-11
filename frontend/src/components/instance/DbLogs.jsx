import LogsWindow from "../LogsWindow";
import {useEffect, useState} from "react";
import DbLogsRequest from "../../api/DbLogsRequest";
import {useParams} from "react-router-dom";

function DbLogs(){
    const [data, setData] = useState("");

    const {instanceId} = useParams();

    /*useEffect(() => {
        const fetchData = () => {
            DbLogsRequest(instanceId).then(result => {
                setData(result.result);
            }).catch(error => console.log(error));
        };

        fetchData();

        const intervalId = setInterval(fetchData, 5000); // Обновление каждые 5 секунд

        return () => clearInterval(intervalId); // Очистка интервала при размонтировании компонента
    }, []);*/

    return <LogsWindow content={data}/>;
}

export default DbLogs;