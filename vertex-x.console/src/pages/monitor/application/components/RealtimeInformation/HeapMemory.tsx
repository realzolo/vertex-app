import { Card } from "antd";
import { Area } from '@ant-design/plots';
import { useEffect, useState } from "react";
import { getApplicationInformation } from "@/services/monitor/application.api";
import moment from "moment";

const HeapMemory = () => {
  const [heapMemoryMax, setHeapMemoryMax] = useState<number>(0);
  const [heapMemoryCommitted, setHeapMemoryCommitted] = useState<Model.Monitor.TimePoint[]>([]);
  const [heapMemoryUsed, setHeapMemoryUsed] = useState<Model.Monitor.TimePoint[]>([]);

  useEffect(() => {
    const timer = setInterval(() => {
      getApplicationInformation('metrics/jvm.memory.max', 'area:heap').then(res => {
        setHeapMemoryMax(res.data?.measurements?.[0]?.value || 0);
      });
      getApplicationInformation('metrics/jvm.memory.committed', 'area:heap').then(res => {
        setHeapMemoryCommitted([...heapMemoryCommitted, newTimePoint(res.data?.measurements?.[0]?.value || 0)]);
      });
      getApplicationInformation('metrics/jvm.memory.used', 'area:heap').then(res => {
        setHeapMemoryUsed([...heapMemoryUsed, newTimePoint(res.data?.measurements?.[0]?.value || 0)]);
      });
    }, 1000);
    return () => {
      clearInterval(timer);
    };
  }, []);

  const newTimePoint = (value: number) => {
    return {
      time: moment().format('HH:mm:ss'),
      value: value
    } as Model.Monitor.TimePoint;
  }

  const config: {} = {
    // data: {
    //   type: 'fetch',
    //   value: 'https://assets.antv.antgroup.com/g2/unemployment-by-industry.json',
    // },
    // xField: (d: any) => new Date(d.date),
    // yField: 'unemployed',
    // colorField: 'industry',
    // shapeField: 'smooth',
    // stack: true, // Try to remove this line.
  };
  return (
    <Card title="内存: Heap">
      <Area  {...config} />
    </Card>
  );
}

export default HeapMemory;
