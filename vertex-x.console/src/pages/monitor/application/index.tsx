import { Tabs, TabsProps } from "antd";
import { useState } from "react";
import RealtimeInformation from "@/pages/monitor/application/components/RealtimeInformation";

const items: TabsProps['items'] = [
  {
    key: 'RealtimeInformation',
    label: '实时信息',
    children: <RealtimeInformation/>,
  },
];

const ApplicationMonitorPage = () => {
  const [activeKey, setActiveKey] = useState(items[0].key);

  const changeTab = (key: string) => {
    setActiveKey(key);
  }

  return (
    <div>
      <Tabs activeKey={activeKey} defaultActiveKey={items[0].key} items={items} onChange={changeTab}/>
    </div>
  );
}

export default ApplicationMonitorPage;
