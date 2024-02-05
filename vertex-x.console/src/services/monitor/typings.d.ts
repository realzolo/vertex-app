declare namespace M.Monitor {
  type TimePoint = {
    type: string;
    time: string;
    value: number;
  }
}

declare namespace Rt.Security {

}

declare namespace Rs.Monitor {
  type RedisInfo = {
    commandStats: Record<string, string>[],
    dbSize: number,
    info: Record<string, string | number>,
  }
}
