//�S�p�J�i�𔼊p�J�i�ɕϊ�
function toHankaku(motoText){
    
	han = "�������������������������������������������ܦݧ�����������!?()";
	han+= "���������������������";
	han+= "�����";
//  0�`61
	txt = "�A�C�E�G�I�J�L�N�P�R�T�V�X�Z�\�^�`�c�e�g�i�j�k�l�m�n�q�t�w�z�}�~�����������������������������@�B�D�F�H�������b�[�u�v�I�H�i�j"
//  62�`82
	txt+= "���K�M�O�Q�S�U�W�Y�[�]�_�a�d�f�h�o�r�u�x�{";
//�@83�`87
	txt+= "�p�s�v�y�|";
	str = "";
	for (i=0; i<motoText.length; i++)
	{
	c = motoText.charAt(i);
	n = txt.indexOf(c,0);
	if (n >= 0) 
		{
		c = han.charAt(n);
		}
	str += c;
	if (n >= 83) 
		{
		str += "�";
		}
	else if (n  >= 62)
		{
		str += "�";
		}
	}
	return str;
}
