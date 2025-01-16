import { Item } from './item';

export class user_Items {
  id: string;
  originalItem: Item;
  isChecked: boolean;
  createdAt: Date;
  updatedAt: Date;

  constructor(
    id: string,
    originalItem: Item,
    isChecked: boolean,
    createdAt: Date,
    updatedAt: Date
  ) {
    this.id = id;
    this.originalItem = originalItem;
    this.isChecked = isChecked;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }
}

