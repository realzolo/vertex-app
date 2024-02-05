/**
 * 通用的、无法归类的工具函数
 */
import { message as AntdMessage } from 'antd';

type MessageMethods = 'info' | 'success' | 'error' | 'warning' | 'loading';
/**
 * antd message
 * @param method 'info' | 'success' | 'error' | 'warning' | 'loading'
 * @param message 消息内容
 */
export const antdMessage = (method: MessageMethods, message: string) => {
  AntdMessage.config({
    maxCount: 1,
  });
  AntdMessage[method](message).then();
}
