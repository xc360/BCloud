import {Pipe, PipeTransform} from '@angular/core';
import {LoginTool} from '@ccxc/tool';

/**
 * 描述：处理获取字典描述，有缓存
 * 作者：xc
 * 时间：2020-08-13 10:33
 */
@Pipe({
  name: 'BAuthority'
})
export class ButtonAuthorityPipe implements PipeTransform {

  constructor() {
  }

  transform(value: any, groupId?: any): any {
    if (value) {
      if (groupId) {
        return LoginTool.verifyGroupAuthority(groupId, value);
      } else {
        return LoginTool.verifyAuthority(value);
      }

    }
    return true;
  }
}
