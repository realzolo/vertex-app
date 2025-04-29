export enum NodeType {
  START = 0,
  APPROVER = 1,
  CC = 2,
  END = 3,
}

export interface EdgeEmitData {
  edgeId: string
  source: string
  target: string
  position: { x: number, y: number }
  type: number
}
