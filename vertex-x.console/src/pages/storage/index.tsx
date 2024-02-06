import { Tabs, TabsProps } from "antd";
import { useState } from "react";
import FileList from "@/pages/storage/components/FileList";
import StorageManage from "@/pages/storage/components/StorageManage";

const items: TabsProps['items'] = [
  {
    key: 'FileList',
    label: '文件列表',
    children: <FileList/>,
  },
  {
    key: 'StorageManage',
    label: '存储策略',
    children: <StorageManage/>,
  },
];

const FileStoragePage = () => {
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

export default FileStoragePage;
