export class User {
  username: string;
  password?: string;
  createdAt?: Date;
  updatedAt?: Date;
  role?: Role;
  enabled?: boolean;
  authorities?: string[]; // Accept authorities as strings

  constructor(
    username: string,
    password: string,
    createdAt?: Date,
    updatedAt?: Date,
    role?: Role,
    enabled?: boolean,
    authorities?: string[]
  ) {
    this.username = username;
    this.password = password;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.role = role;
    this.enabled = enabled;
    this.authorities = authorities || []; // Default to empty array
  }
}

export enum Role {
  ADMIN = 'ADMIN',
  USER = 'USER',
  SUPER_ADMIN = 'SUPER_ADMIN'
}
