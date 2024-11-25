import {optionalRequest, RequestMethods} from "@/utils/RequestTool.js";

export default async function getBlobData(URL) {
    return await optionalRequest({
        method: RequestMethods.GET,
        url: URL,
        responseType: 'blob'
    });
}