import React, { useEffect, useState } from "react"
import { PageContainer } from "@ant-design/pro-components";
import CommentCountChart from "./components/CommentCountChart";
import BasicInfoCard from "./components/BasicInfoCard";
import { getRedisInformation } from "@/services/monitor/cache.api";

const CacheMonitorPage = () => {
  const [data, setData] = useState<Rs.Monitor.RedisInfo>();
  const [loading, setLoading] = useState<boolean>(true);

  useEffect(() => {
    setLoading(true);
    fetchData().finally();
    const timer = setInterval(() => {
      fetchData().finally();
    }, 10000)
    setLoading(false);

    return () => {
      clearInterval(timer);
    }
  }, [])

  const fetchData = async () => {
    // setLoading(true);
    const resp = await getRedisInformation();
    if (resp?.success) {
      setData(resp.data);
    }
    // setLoading(false);
  }

  if (!data) {
    return (
      <PageContainer loading={loading}/>
    );
  }

  const {commandStats, dbSize, info} = data;

  return (
    <PageContainer loading={loading}>
      <BasicInfoCard data={{...info, dbSize}}/>
      <CommentCountChart data={commandStats}/>
      <BasicInfoCard data={{...info, dbSize}}/>
      <CommentCountChart data={commandStats}/>
    </PageContainer>
  )

}

export default CacheMonitorPage;
