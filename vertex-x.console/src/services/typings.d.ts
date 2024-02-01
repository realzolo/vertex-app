declare namespace API {
  type RestResponse<T> = {
    code: number;
    success: boolean;
    message: string;
    data: T;
    traceId: string;
    timestamp: string;
  };

  type RequestOption = { [key: string]: any };
}
