import {optionalRequest} from '@/utils/RequestTool.js';

export const getFiles = (path) => {
    return optionalRequest({
        method: 'GET',
        url: `/getFiles/${encodeURIComponent(path)}`,
    });
};