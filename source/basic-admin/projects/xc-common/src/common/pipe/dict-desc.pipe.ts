import {Pipe, PipeTransform} from '@angular/core';
import {Observable} from 'rxjs';
import {DictTool} from '@ccxc/tool';

/**
 * 描述：处理获取字典描述，有缓存
 * 作者：xc
 * 时间：2020-08-13 10:33
 */
@Pipe({
  name: 'dictDesc'
})
export class DictDescPipe implements PipeTransform {

  constructor() {
  }

  transform(value: any, dictType?: string, options?: Array<any>): any {
    if (options) {
      return new Observable((observer) => {
        let bool = true;
        for (const option of options) {
          if (option.value + '' === value + '') {
            bool = false;
            observer.next(option.name);
          }
        }
        if (bool) {
          observer.next(value);
        }
      });
    } else {
      return new Observable((observer) => {
        if (dictType) {
          observer.next(DictTool.getDictName(value, dictType));
        } else {
          observer.next(value);
        }
      });
    }
  }
}
