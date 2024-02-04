import moment from "moment";
import { Card } from "antd";
import { Area } from '@ant-design/plots';
import { useEffect, useRef, useState } from "react";
import { getApplicationInformation } from "@/services/monitor/application.api";

const HeapMemory = () => {
  const [heapMemoryMax, setHeapMemoryMax] = useState<number>(0);
  const [heapMemory, setHeapMemory] = useState<M.Monitor.TimePoint[]>([]);
  const heapMemoryArrayRef = useRef<M.Monitor.TimePoint[]>([]);

  useEffect(() => {
    fetchData();
    const timer = setInterval(() => {
      fetchData();
    }, 3000);

    return () => {
      clearInterval(timer);
    };
  }, []);

  const fetchData = () => {
    getApplicationInformation('metrics/jvm.memory.max', 'area:heap').then(res => {
      setHeapMemoryMax(res.data?.measurements?.[0]?.value || 0);
    });
    getApplicationInformation('metrics/jvm.memory.committed', 'area:heap').then(res => {
      heapMemoryArrayRef.current.push(newTimePoint('committed', calcValue(res.data?.measurements?.[0]?.value || 0)));
    });
    getApplicationInformation('metrics/jvm.memory.used', 'area:heap').then(res => {
      heapMemoryArrayRef.current.push(newTimePoint('used', calcValue(res.data?.measurements?.[0]?.value || 0)));
    });
  }

  useEffect(() => {
    setTimeout(() => {
      setHeapMemory([...heapMemoryArrayRef.current]);
    }, 2000);
    const timer = setInterval(() => {
      if (heapMemoryArrayRef.current.length > 8) {
        heapMemoryArrayRef.current.shift();
        heapMemoryArrayRef.current.shift();
      }
      setHeapMemory([...heapMemoryArrayRef.current]);
    }, 3000);

    return () => {
      clearInterval(timer);
    };
  }, []);

  const newTimePoint = (type: string, value: number) => {
    return {
      type: type,
      time: moment().format('mm:ss'),
      value: value
    } as M.Monitor.TimePoint;
  }

  const calcValue = (value: number) => {
    return parseFloat((value / 1024 / 1024).toFixed(2));
  }

  const config = {
    data: heapMemory,
    animate: false,
    xField: 'time',
    yField: 'value',
    colorField: 'type',
    shapeField: 'smooth',
  };

  return (
    <Card title="内存: Heap">
      <Area  {...config}/>
    </Card>
  );
}

export default HeapMemory;
