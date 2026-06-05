import {Pipe, PipeTransform} from '@angular/core';
import {CommonTool} from '@ccxc/tool';

@Pipe({
  name: 'i18n'
})
export class SystemI18nPipe implements PipeTransform {

  public i18n = {};

  constructor() {
  }

  transform(value: any, type: any): any {
    const data = CommonTool.getObjectField(this.i18n, type);
    if (CommonTool.notNull(data)) {
      return data;
    } else {
      return value;
    }
  }

  public setI18n(i18n) {
    this.i18n = i18n;
  }
}

