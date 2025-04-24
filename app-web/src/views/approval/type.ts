export enum NodeType {
  START = 0,
  CC = 1,
  APPROVER = 2,
  END,
}

export interface EdgeEmitData {
  edgeId: string
  source: string
  target: string
  position: { x: number, y: number }
  type: number
}
