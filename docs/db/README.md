MySQLのテーブル定義から直接可読性の高いドキュメントを出力することも検討しましたが、それだと「いつ誰がどのような目的でテーブルに対して変更を加えたのかの履歴が管理できない」ということで、「リポジトリ側でテーブル定義書を管理し、コードレビューと同様に変更時にはレビューを行い、リリース時に実際のDBの方にも変更を反映させる」方が良いだろうということになりました。