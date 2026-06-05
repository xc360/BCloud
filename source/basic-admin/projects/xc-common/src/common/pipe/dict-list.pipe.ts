import {Pipe, PipeTransform} from '@angular/core';
import {Observable} from 'rxjs';
import {DictTool} from '@ccxc/tool';

/**
 * 描述：处理获取字典集合,实时，无缓存
 * 作者：xc
 * 时间：2020-08-13 10:33
 */
@Pipe({
  name: 'dictList'
})
export class DictListPipe implements PipeTransform {

  constructor() {
  }

  transform(dictType: string, options?: Array<any>): any {
    return new Observable((observer) => {
      if (options) {
        observer.next(options);
      } else {
        observer.next(DictTool.getDictList(dictType));
      }
    });
  }
}
